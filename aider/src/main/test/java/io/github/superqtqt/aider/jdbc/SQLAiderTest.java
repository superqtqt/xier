package io.github.superqtqt.aider.jdbc;


import org.junit.Test;

public class SQLAiderTest{

    @Test
    public void split(){
        final String sql1="select 1 from '1;';1sdsd";
        for (String s : SQLAider.split(sql1, ';')) {
            System.out.println(s);
        }
    }
}