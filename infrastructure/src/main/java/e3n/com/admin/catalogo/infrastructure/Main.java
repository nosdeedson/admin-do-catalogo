package e3n.com.admin.catalogo.infrastructure;

import e3n.com.admin.catalogo.application.UseCase;
import e3n.com.admin.catalogo.domain.Category;

public class Main {
    public static void main(String[] args) {
        System.out.println(new Category("teste"));
        System.out.println(new UseCase().testeCategory());
    }
}