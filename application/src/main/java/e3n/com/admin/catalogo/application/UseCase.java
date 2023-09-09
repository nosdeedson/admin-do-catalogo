package e3n.com.admin.catalogo.application;

import e3n.com.admin.catalogo.domain.category.Category;


public abstract class UseCase<IN, OUT> {

    public abstract OUT execute(IN in);
}