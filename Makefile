all:
	javac -d bin -cp ./ src/*.java

run:
	java -cp bin Slow
