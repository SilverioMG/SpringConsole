package net.atopecode.springconsole;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import javax.persistence.EntityManager;

public class Application {

    public static void main(String[] args){
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(RepositoryConfig.class);
        //context.refresh();
        //context.register(RepositoryConfig.class);

        LocalContainerEntityManagerFactoryBean emFactory = context.getBean(LocalContainerEntityManagerFactoryBean.class);
        EntityManager em1 =  emFactory.getObject().createEntityManager();
        EntityManager em2 = emFactory.getObject().createEntityManager();

        if(em1 == em2){
            System.out.println("Mismo Objeto...");
        }
        else{
            System.out.println("Distinto Objeto...");
        }
    }
}
