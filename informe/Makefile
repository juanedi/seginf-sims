informe: 
	@echo "Compilando"
	@cd src; \
	pdflatex --interaction batchmode informe.tex > /dev/null; \
	pdflatex informe.tex; \
	rm -f *.toc *.out *.aux *.log
	@mv src/informe.pdf .
	@echo "Listo"
	                      
clean:
	rm -f informe.pdf 
