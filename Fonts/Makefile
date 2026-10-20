SRC_DIR := src
BIN_DIR := bin
LIB_DIR := lib
GSON_JAR := $(LIB_DIR)/gson-2.10.1.jar

MAIN_CLASS := main
JAR_NAME := HIDATO.jar

SOURCES := $(shell find $(SRC_DIR) -name "*.java" | sort)
CP := $(SRC_DIR):$(GSON_JAR)

.PHONY: all run clean

all: $(JAR_NAME)

$(JAR_NAME): $(SOURCES) | $(BIN_DIR)
	javac -encoding UTF-8 -cp $(CP) -sourcepath $(SRC_DIR) -d $(BIN_DIR) $(SOURCES)
	jar cfe $(JAR_NAME) $(MAIN_CLASS) -C $(BIN_DIR) .

$(BIN_DIR):
	mkdir -p $(BIN_DIR)

run: $(JAR_NAME)
	java -cp "$(JAR_NAME):$(GSON_JAR)" $(MAIN_CLASS)

clean:
	rm -rf $(BIN_DIR) $(JAR_NAME)
