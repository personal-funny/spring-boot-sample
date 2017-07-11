package com.lee.boot.common.converter;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.ResolvableType;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractGenericHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJacksonInputMessage;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.util.*;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.nio.charset.Charset;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Jackson消息转换器
 * Created by taoping on 2016/8/26.
 */
public class Jackson2HttpMessageConverter extends AbstractGenericHttpMessageConverter<Object> implements InitializingBean {
    public static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");
    private static final String EXCLUDE_PRE_PACKAGE = "springfox.documentation";
    private static final String EXLUDE_PRE_URI = "/swagger";
    private static final Logger logger = LoggerFactory.getLogger(Jackson2HttpMessageConverter.class);
    protected ObjectMapper objectMapper;
    private Boolean prettyPrint;
    private String jsonPrefix;

    private Boolean webapiFlag;

    private String ignorePatterns;
    private String[] ignorePaths;

    private PathMatcher ignorePathMatcher;

    public Jackson2HttpMessageConverter() {
        this(Jackson2ObjectMapperBuilder.json().build());
    }

    public Jackson2HttpMessageConverter(ObjectMapper objectMapper) {
        this(objectMapper, MediaType.APPLICATION_JSON, MediaType.ALL.APPLICATION_JSON_UTF8, MediaType.TEXT_PLAIN, new MediaType("application", "*+json"));
    }

    protected Jackson2HttpMessageConverter(ObjectMapper objectMapper, MediaType supportedMediaType) {
        super(supportedMediaType);
        this.objectMapper = objectMapper;
        setDefaultCharset(DEFAULT_CHARSET);
    }

    protected Jackson2HttpMessageConverter(ObjectMapper objectMapper, MediaType... supportedMediaTypes) {
        super(supportedMediaTypes);
        this.objectMapper = objectMapper;
        setDefaultCharset(DEFAULT_CHARSET);
    }

    /**
     * Specify a custom prefix to use for this view's JSON output.
     * Default is none.
     *
     * @see #setPrefixJson
     */
    public void setJsonPrefix(String jsonPrefix) {
        this.jsonPrefix = jsonPrefix;
    }

    /**
     * Indicate whether the JSON output by this view should be prefixed with ")]}', ". Default is false.
     * <p>Prefixing the JSON string in this manner is used to help prevent JSON Hijacking.
     * The prefix renders the string syntactically invalid as a script so that it cannot be hijacked.
     * This prefix should be stripped before parsing the string as JSON.
     *
     * @see #setJsonPrefix
     */
    public void setPrefixJson(boolean prefixJson) {
        this.jsonPrefix = (prefixJson ? ")]}', " : null);
    }

    /**
     * Return the underlying {@code ObjectMapper} for this view.
     */
    public ObjectMapper getObjectMapper() {
        return this.objectMapper;
    }

    /**
     * Set the {@code ObjectMapper} for this view.
     * If not set, a default {@link ObjectMapper#ObjectMapper() ObjectMapper} is used.
     * <p>Setting a custom-configured {@code ObjectMapper} is one way to take further
     * control of the JSON serialization process. For example, an extended
     * {@link com.fasterxml.jackson.databind.ser.SerializerFactory}
     * can be configured that provides custom serializers for specific types.
     * The other option for refining the serialization process is to use Jackson's
     * provided annotations on the types to be serialized, in which case a
     * custom-configured ObjectMapper is unnecessary.
     */
    public void setObjectMapper(ObjectMapper objectMapper) {
        Assert.notNull(objectMapper, "ObjectMapper must not be null");
        this.objectMapper = objectMapper;
        configurePrettyPrint();
    }

    /**
     * Whether to use the {@link DefaultPrettyPrinter} when writing JSON.
     * This is a shortcut for setting up an {@code ObjectMapper} as follows:
     * <pre class="code">
     * ObjectMapper mapper = new ObjectMapper();
     * mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
     * converter.setObjectMapper(mapper);
     * </pre>
     */
    public void setPrettyPrint(boolean prettyPrint) {
        this.prettyPrint = prettyPrint;
        configurePrettyPrint();
    }

    private void configurePrettyPrint() {
        if (this.prettyPrint != null) {
            this.objectMapper.configure(SerializationFeature.INDENT_OUTPUT, this.prettyPrint);
        }
    }

    @Override
    public boolean canRead(Class<?> clazz, MediaType mediaType) {
        return canRead(clazz, null, mediaType);
    }

    @Override
    public boolean canRead(Type type, Class<?> contextClass, MediaType mediaType) {
        //不解析text/plan输入，只封装text/plan输出
        if (MediaType.TEXT_PLAIN.includes(mediaType)) {
            return false;
        }
        if (!canRead(mediaType)) {
            return false;
        }
        JavaType javaType = getJavaType(type, contextClass);
        if (!logger.isWarnEnabled()) {
            return this.objectMapper.canDeserialize(javaType);
        }
        AtomicReference<Throwable> causeRef = new AtomicReference<Throwable>();
        if (this.objectMapper.canDeserialize(javaType, causeRef)) {
            return true;
        }
        logWarningIfNecessary(javaType, causeRef.get());
        return false;
    }

    @Override
    public boolean canWrite(Class<?> clazz, MediaType mediaType) {
        if (!supports(clazz)) {
            return false;
        }
        if (!canWrite(mediaType)) {
            return false;
        }
        if (!logger.isWarnEnabled()) {
            return this.objectMapper.canSerialize(clazz);
        }
        AtomicReference<Throwable> causeRef = new AtomicReference<Throwable>();
        if (this.objectMapper.canSerialize(clazz, causeRef)) {
            return true;
        }
        logWarningIfNecessary(clazz, causeRef.get());
        return false;
    }

    /**
     * Determine whether to log the given exception coming from a
     * {@link ObjectMapper#canDeserialize} / {@link ObjectMapper#canSerialize} check.
     *
     * @param type  the class that Jackson tested for (de-)serializability
     * @param cause the Jackson-thrown exception to evaluate
     *              (typically a {@link JsonMappingException})
     * @since 4.3
     */
    protected void logWarningIfNecessary(Type type, Throwable cause) {
        if (cause != null && !(cause instanceof JsonMappingException && cause.getMessage().startsWith("Can not find"))) {
            String msg = "Failed to evaluate Jackson " + (type instanceof JavaType ? "de" : "") +
                    "serialization for type [" + type + "]";
            if (logger.isDebugEnabled()) {
                logger.warn(msg, cause);
            } else {
                logger.warn(msg + ": " + cause);
            }
        }
    }

    @Override
    protected boolean supports(Class<?> clazz) {
        boolean support = clazz.getPackage() == null || !clazz.getPackage().getName().startsWith(EXCLUDE_PRE_PACKAGE);
        if (support) {
            try {
                HttpServletRequest request = ((ServletRequestAttributes) org.springframework.web.context.request.RequestContextHolder.currentRequestAttributes()).getRequest();
                String requestUri = request.getRequestURI();
                if (StringUtils.hasText(requestUri)) {
                    if (requestUri.startsWith(EXLUDE_PRE_URI)) {
                        support = false;
                    }
                    if (support && ArrayUtils.isNotEmpty(ignorePaths)) {
                        for (String patten : ignorePaths) {
                            if (ignorePathMatcher.match(patten, requestUri)) {
                                support = false;
                                break;
                            }
                        }
                    }
                } else {
                    support = true;
                }
            } catch (Exception ex) {
                //TODO 解决远程服务调用在不同线程的问题
                //Thread-37http-nio-9999-exec-1:org.apache.catalina.connector.RequestFacade@4311f0d2
                //Thread-43hystrix-app-push-producer-dev-1:No thread-bound request found
                logger.warn("Thread-{}{}:{}", Thread.currentThread().getId(), Thread.currentThread().getName(), ex.getMessage());
                return false;
            }
        }
        return support;
    }


    @Override
    protected Object readInternal(Class<?> clazz, HttpInputMessage inputMessage)
            throws IOException, HttpMessageNotReadableException {

        JavaType javaType = getJavaType(clazz, null);
        Object inputObject = readJavaType(javaType, inputMessage);
        logger.debug("Input object : {}", inputObject);
        return inputObject;
    }

    @Override
    public Object read(Type type, Class<?> contextClass, HttpInputMessage inputMessage)
            throws IOException, HttpMessageNotReadableException {

        JavaType javaType = getJavaType(type, contextClass);
        return readJavaType(javaType, inputMessage);
    }

    private Object readJavaType(JavaType javaType, HttpInputMessage inputMessage) {
        try {
            if (inputMessage instanceof MappingJacksonInputMessage) {
                Class<?> deserializationView = ((MappingJacksonInputMessage) inputMessage).getDeserializationView();
                if (deserializationView != null) {
                    return this.objectMapper.readerWithView(deserializationView).forType(javaType).
                            readValue(inputMessage.getBody());
                }
            }
            return this.objectMapper.readValue(inputMessage.getBody(), javaType);
        } catch (IOException ex) {
            throw new HttpMessageNotReadableException("Could not read document: " + ex.getMessage(), ex);
        }
    }

    @Override
    protected void writeInternal(Object outputObject, Type type, HttpOutputMessage outputMessage)
            throws IOException, HttpMessageNotWritableException {
        logger.debug("===>>Output object : {},{}", outputObject, type);

        if (!(outputObject instanceof JsonResponse)) {
            outputObject = LocalJsonResponse.ok(outputObject);
        }

        JsonEncoding encoding = getJsonEncoding(outputMessage.getHeaders().getContentType());
        JsonGenerator generator = this.objectMapper.getFactory().createGenerator(outputMessage.getBody(), encoding);
        try {
            writePrefix(generator, outputObject);

            Class<?> serializationView = null;
            FilterProvider filters = null;
            Object value = outputObject;
            JavaType javaType = null;
            if (outputObject instanceof MappingJacksonValue) {
                MappingJacksonValue container = (MappingJacksonValue) outputObject;
                value = container.getValue();
                serializationView = container.getSerializationView();
                filters = container.getFilters();
            }
            if (type != null && value != null && TypeUtils.isAssignable(type, value.getClass())) {
                javaType = getJavaType(type, null);
            }
            ObjectWriter objectWriter;
            if (serializationView != null) {
                objectWriter = this.objectMapper.writerWithView(serializationView);
            } else if (filters != null) {
                objectWriter = this.objectMapper.writer(filters);
            } else {
                objectWriter = this.objectMapper.writer();
            }
            if (javaType != null && javaType.isContainerType()) {
                objectWriter = objectWriter.forType(javaType);
            }
            objectWriter.writeValue(generator, value);

            writeSuffix(generator, outputObject);
            generator.flush();

        } catch (JsonProcessingException ex) {
            throw new HttpMessageNotWritableException("Could not write content: " + ex.getMessage(), ex);
        }
    }

    /**
     * Write a prefix before the main content.
     *
     * @param generator the generator to use for writing content.
     * @param object    the object to write to the output message.
     */
    protected void writePrefix(JsonGenerator generator, Object object) throws IOException {
        if (this.jsonPrefix != null) {
            generator.writeRaw(this.jsonPrefix);
        }
        String jsonpFunction =
                (object instanceof MappingJacksonValue ? ((MappingJacksonValue) object).getJsonpFunction() : null);
        if (jsonpFunction != null) {
            generator.writeRaw("/**/");
            generator.writeRaw(jsonpFunction + "(");
        }
    }

    /**
     * Write a suffix after the main content.
     *
     * @param generator the generator to use for writing content.
     * @param object    the object to write to the output message.
     */
    protected void writeSuffix(JsonGenerator generator, Object object) throws IOException {
        String jsonpFunction =
                (object instanceof MappingJacksonValue ? ((MappingJacksonValue) object).getJsonpFunction() : null);
        if (jsonpFunction != null) {
            generator.writeRaw(");");
        }
    }

    /**
     * Return the Jackson {@link JavaType} for the specified type and context class.
     * <p>The default implementation returns {@code typeFactory.constructType(type, contextClass)},
     * but this can be overridden in subclasses, to allow for custom generic collection handling.
     * For instance:
     * <pre class="code">
     * protected JavaType getJavaType(Type type) {
     * if (type instanceof Class && List.class.isAssignableFrom((Class)type)) {
     * return TypeFactory.collectionType(ArrayList.class, MyBean.class);
     * } else {
     * return super.getJavaType(type);
     * }
     * }
     * </pre>
     *
     * @param type         the generic type to return the Jackson JavaType for
     * @param contextClass a context class for the target type, for example a class
     *                     in which the target type appears in a method signature (can be {@code null})
     * @return the Jackson JavaType
     */
    protected JavaType getJavaType(Type type, Class<?> contextClass) {
        TypeFactory typeFactory = this.objectMapper.getTypeFactory();
        if (contextClass != null) {
            ResolvableType resolvedType = ResolvableType.forType(type);
            if (type instanceof TypeVariable) {
                ResolvableType resolvedTypeVariable = resolveVariable(
                        (TypeVariable<?>) type, ResolvableType.forClass(contextClass));
                if (resolvedTypeVariable != ResolvableType.NONE) {
                    return typeFactory.constructType(resolvedTypeVariable.resolve());
                }
            } else if (type instanceof ParameterizedType && resolvedType.hasUnresolvableGenerics()) {
                ParameterizedType parameterizedType = (ParameterizedType) type;
                Class<?>[] generics = new Class<?>[parameterizedType.getActualTypeArguments().length];
                Type[] typeArguments = parameterizedType.getActualTypeArguments();
                for (int i = 0; i < typeArguments.length; i++) {
                    Type typeArgument = typeArguments[i];
                    if (typeArgument instanceof TypeVariable) {
                        ResolvableType resolvedTypeArgument = resolveVariable(
                                (TypeVariable<?>) typeArgument, ResolvableType.forClass(contextClass));
                        if (resolvedTypeArgument != ResolvableType.NONE) {
                            generics[i] = resolvedTypeArgument.resolve();
                        } else {
                            generics[i] = ResolvableType.forType(typeArgument).resolve();
                        }
                    } else {
                        generics[i] = ResolvableType.forType(typeArgument).resolve();
                    }
                }
                return typeFactory.constructType(ResolvableType.
                        forClassWithGenerics(resolvedType.getRawClass(), generics).getType());
            }
        }
        return typeFactory.constructType(type);
    }

    private ResolvableType resolveVariable(TypeVariable<?> typeVariable, ResolvableType contextType) {
        ResolvableType resolvedType;
        if (contextType.hasGenerics()) {
            resolvedType = ResolvableType.forType(typeVariable, contextType);
            if (resolvedType.resolve() != null) {
                return resolvedType;
            }
        }
        resolvedType = resolveVariable(typeVariable, contextType.getSuperType());
        if (resolvedType.resolve() != null) {
            return resolvedType;
        }
        for (ResolvableType ifc : contextType.getInterfaces()) {
            resolvedType = resolveVariable(typeVariable, ifc);
            if (resolvedType.resolve() != null) {
                return resolvedType;
            }
        }
        return ResolvableType.NONE;
    }

    /**
     * Determine the JSON encoding to use for the given content type.
     *
     * @param contentType the media type as requested by the caller
     * @return the JSON encoding to use (never {@code null})
     */
    protected JsonEncoding getJsonEncoding(MediaType contentType) {
        if (contentType != null && contentType.getCharset() != null) {
            Charset charset = contentType.getCharset();
            for (JsonEncoding encoding : JsonEncoding.values()) {
                if (charset.name().equals(encoding.getJavaName())) {
                    return encoding;
                }
            }
        }
        return JsonEncoding.UTF8;
    }

    @Override
    protected MediaType getDefaultContentType(Object object) throws IOException {
        if (object instanceof MappingJacksonValue) {
            object = ((MappingJacksonValue) object).getValue();
        }
        return super.getDefaultContentType(object);
    }

    @Override
    protected Long getContentLength(Object object, MediaType contentType) throws IOException {
        if (object instanceof MappingJacksonValue) {
            object = ((MappingJacksonValue) object).getValue();
        }
        return super.getContentLength(object, contentType);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (StringUtils.isEmpty(ignorePatterns)) {
            return;
        }
        logger.info("===>>Ignore patterns : {}", ignorePatterns);
        ignorePaths = ignorePatterns.split(",");
        ignorePathMatcher = new AntPathMatcher();
    }
}
