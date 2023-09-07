package e3n.com.admin.catalogo.application;

import e3n.com.admin.catalogo.domain.Category;

public class UseCase {

    public Category testeCategory(){
        return new Category("teste");
    }
}