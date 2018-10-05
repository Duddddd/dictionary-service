# dictionary-service
A TCP server that used as dictionary from a key string to a list of strings.

To run the server go to com.dictionaryservice.listener.TCPListener class and run it. You can choose the port that the server will listen by adding it to the running argument or it will use port 1987 by default.

The cutimized protocol that the server uses is as follow:
accept:
[method name],[arg1(string)], [arg2(string)] [\n]
returns:
"succes", if the method don't have return value
[Value1],[Value2],[Value3]...[\n] when there are values to return

for example
request:
get,key1\n
return
value1,value2\n

