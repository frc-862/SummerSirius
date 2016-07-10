[ -z "$JAVA_HOME" ] && echo "JAVA_HOME is not defined. Please set the environment variable JAVA_HOME to a JDK" && exit 1;

if [ ! -f "$JAVA_HOME/bin/javac" ]; then
  echo "JAVA_HOME is not a valid JDK. Please install a JDK and set JAVA_HOME to it"
  echo ""
  echo "JAVA_HOME is currently set to $JAVA_HOME"
fi

"$JAVA_HOME/bin/java" -version
echo ""
"$JAVA_HOME/bin/javac" -version
echo ""
echo "Everything seems fine"

read -n1 -r -p "Press any key to continue..." key