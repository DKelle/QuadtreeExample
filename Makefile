all:
	javac -d bin -cp ./ src/*.java

slow:
	java -cp bin Slow

fast:
	java -cp bin Fast
