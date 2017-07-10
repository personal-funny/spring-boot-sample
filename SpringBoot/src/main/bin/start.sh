#!/bin/bash
cd `dirname $0`
BIN_DIR=`pwd`
cd ..
DEPLOY_DIR=`pwd`
CONF_DIR=$DEPLOY_DIR/conf

SERVER_NAME=`sed '/server.name/!d;s/.*=//' conf/application.properties | tr -d '\r'`
SERVER_PORT=`sed '/server.port/!d;s/.*=//' conf/application.properties | tr -d '\r'`
LOGS_FILE=`sed '/server.log4j.file/!d;s/.*=//' conf/application.properties | tr -d '\r'`

if [ -z "$SERVER_NAME" ]; then
    SERVER_NAME=`hostname`
fi

PIDS=`ps -ef | grep java | grep "$CONF_DIR" |awk '{print $2}'`
if [ -n "$PIDS" ]; then
    echo "ERROR: The $SERVER_NAME already started!"
    echo "PID: $PIDS"
    exit 1
fi

if [ -n "$SERVER_PORT" ]; then
    SERVER_PORT_COUNT=`netstat -tln | grep $SERVER_PORT | wc -l`
    if [ $SERVER_PORT_COUNT -gt 0 ]; then
        echo "ERROR: The $SERVER_NAME port $SERVER_PORT already used!"
        exit 1
    fi
fi

LOGS_DIR=""
if [ -n "$LOGS_FILE" ]; then
    LOGS_DIR=`dirname $LOGS_FILE`
else
    LOGS_DIR=$DEPLOY_DIR/logs
fi
if [ ! -d $LOGS_DIR ]; then
    mkdir $LOGS_DIR
fi
STDOUT_FILE=$LOGS_DIR/stdout.log

LIB_DIR=$DEPLOY_DIR/lib
LIB_JARS=`ls $LIB_DIR|grep .jar|awk '{print "'$LIB_DIR'/"$0}'|tr "\n" ":"`

JAVA_OPTS=" -Djava.awt.headless=true -Djava.net.preferIPv4Stack=true "
JAVA_DEBUG_OPTS=""
if [ "$1" = "debug" ]; then
    JAVA_DEBUG_OPTS=" -Xdebug -Xnoagent"
    JAVA_DEBUG_OPTS="$JAVA_DEBUG_OPTS -Djava.compiler=NONE"
    JAVA_DEBUG_OPTS="$JAVA_DEBUG_OPTS -Xrunjdwp:transport=dt_socket,address=8000,server=y,suspend=n"
fi
JAVA_JX_OPTS=""
if [ "$1" = "jmx" ]; then
    JAVA_JX_OPTS=" -Dcom.sun.management.jmxremote.port=1099"
    JAVA_JX_OPTS="$JAVA_JX_OPTS -Dcom.sun.management.jmxremote.ssl=false"
    JAVA_JX_OPTS="$JAVA_JX_OPTS -Dcom.sun.management.jmxremote.authenticate=false"
fi
JAVA_E_OPTS=""
BITS=`java -version 2>&1 | grep -i 64-bit`
if [ -n "$BITS" ]; then
    JAVA_E_OPTS=" -server -Xmx2g -Xms2g -Xmn256m -Xss512k -XX:+DisableExplicitGC  "
else
    JAVA_E_OPTS=" -server -Xms1g -Xmx1g -XX:SurvivorRatio=2 -XX:+UseParallelGC "
fi

echo "JAVA_HOME=$JAVA_HOME"
if [ $JAVA_HOME == "" ]; then
    exit 1
fi

echo -e "Starting the $SERVER_NAME ...\c"
nohup $JAVA_HOME/bin/java $JAVA_OPTS $JAVA_E_OPTS $JAVA_DEBUG_OPTS $JAVA_JX_OPTS -classpath \
    $CONF_DIR:$LIB_JARS com.lee.boot.web.App > $STDOUT_FILE 2>&1 &

COUNT=0

echo "OK!"
PIDS=`ps -ef | grep java | grep "$DEPLOY_DIR" | awk '{print $2}'`
echo "PID: $PIDS"
echo "STDOUT: $STDOUT_FILE"
