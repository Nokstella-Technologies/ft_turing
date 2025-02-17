JAVA_CHECK := $(shell command -v java 2>/dev/null)
LEIN_CHECK := $(shell command -v lein 2>/dev/null)

# URL para baixar o Leiningen, caso não esteja instalado
LEIN_URL := https://raw.githubusercontent.com/technomancy/leiningen/stable/bin/lein

# Regras principais
all: check-java check-lein run

# Regra para verificar se o Java está instalado
check-java:
ifndef JAVA_CHECK
	@echo "Java não encontrado. Instalando..."
	sudo apt update && sudo apt install -y default-jre
else
	@echo "Java encontrado em: $(JAVA_CHECK)"
endif

# Regra para verificar se o Leiningen está instalado
check-lein:
ifndef LEIN_CHECK
	@echo "Leiningen não encontrado. Instalando..."
	sudo wget $(LEIN_URL) -O /usr/local/bin/lein
	sudo chmod +x /usr/local/bin/lein
	lein
else
	@echo "Leiningen encontrado em: $(LEIN_CHECK)"
endif

# Regra para executar o programa
run:
	@echo "try with this : resources/inception_machine.json \"@[1+=.]%[abcds]|a&a{[+b>1][1a>1]}b{[1b>1][=c>=]}c{[.d<.]}d{[1s>.][=d<.]}*111+11=\""
	lein run -m ft-turing.core -h



# Regras auxiliares
clean:
	@echo "Limpeza de arquivos temporários..."

fclean: clean
	@echo "Remoção de arquivos gerados..."

re: fclean all

.PHONY: all check-java check-lein run clean fclean re