JAVA_CHECK := $(shell command -v java 2>/dev/null)
LEIN := $(shell command -v lein 2>/dev/null)
LEIN_URL =  "https://raw.githubusercontent.com/technomancy/leiningen/stable/bin/lein"

all: $(JAVA) $(LEIN) run

$(JAVA):
	sudo apt update
	sudo apt install default-jre

$(LEIN):
	sudo wget $(LEIN_URL)
	./lein

run:
	lein run -m ft-turing.core -h

clean:

fclean:

re:

.PHONY: all run