package br.projeto.model;

/**
 * Entidade de domínio que representa uma mídia (filme ou livro).
 
 * Campos: id, titulo, autor, tipo (FILME/LIVRO), ano, nota (0..5), sinopse, posterUrl.
 * A nota é armazenada como {@code Double} (pode ser {@code null}), porém a UI usa inteiros 0..5.
 * O {@code posterUrl} aceita URL direta de imagem ou caminho local em {@code assets/...}.
 
 */
public class Midia {
    private Integer id;
    private String  titulo;
    private String  autor;    
    private String  tipo;      // FILME / LIVRO
    private Integer ano;
    private Double  nota;
    private String  sinopse;
    private String  posterUrl;

    /** Construtor padrão. */
    public Midia() {}

    /**
     * Construtor completo.
     
     * @param id        identificador
     * @param titulo    título (obrigatório)
     * @param autor     autor (obrigatório)
     * @param tipo      "FILME" ou "LIVRO"
     * @param ano       ano de lançamento/publicação
     * @param nota      nota 0..5 (pode ser null)
     * @param sinopse   sinopse (opcional)
     * @param posterUrl URL direta de imagem ou caminho local em assets (opcional)
     */
    public Midia(Integer id, String titulo, String autor, String tipo, Integer ano,
                 Double nota, String sinopse, String posterUrl) {
        this.id = id;
        this.titulo = titulo;
        this.autor = autor;
        this.tipo = tipo;
        this.ano = ano;
        this.nota = nota;
        this.sinopse = sinopse;
        this.posterUrl = posterUrl;
    }

    // Getters/Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getAutor() { return autor; }
    public void setAutor(String autor) { this.autor = autor; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public Integer getAno() { return ano; }
    public void setAno(Integer ano) { this.ano = ano; }

    public Double getNota() { return nota; }
    public void setNota(Double nota) { this.nota = nota; }

    public String getSinopse() { return sinopse; }
    public void setSinopse(String sinopse) { this.sinopse = sinopse; }

    public String getPosterUrl() { return posterUrl; }
    public void setPosterUrl(String posterUrl) { this.posterUrl = posterUrl; }
}
