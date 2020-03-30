package com.kalendersoftware.pocketpassword.Objects;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.internal.matchers.apachecommons.ReflectionEquals;

public class ResultObjectTest {

    @Test
    public void constructorNull(){
        ResultObject excepted = new ResultObject(false, null, null);
        ResultObject actual= new ResultObject();

        Assert.assertTrue(new ReflectionEquals(excepted).matches(actual));
    }

    @Test
    public void constructorWithStatus(){
        ResultObject excepted = new ResultObject(true, null, null);
        ResultObject actual = new ResultObject(true);

        Assert.assertTrue(new ReflectionEquals(excepted).matches(actual));
    }

    @Test
    public void constructorWithStatusAndData(){
        ResultObject excepted = new ResultObject(true, null, "TEST");
        ResultObject actual = new ResultObject(true).setData("TEST");

        Assert.assertTrue(new ReflectionEquals(excepted).matches(actual));
    }

    @Test
    public void constructorWithStatusAndDataWrong(){
        //setData, Statüsü true yapar,  o yüzden false

        ResultObject excepted = new ResultObject(false, null, "TEST");
        ResultObject actual = new ResultObject(false).setData("TEST");

        Assert.assertFalse(new ReflectionEquals(excepted).matches(actual));
    }

    @Test
    public void constructorWithStatusAndDataForErrorWrong(){
        //setError, Statüsü true yapar,  o yüzden false

        Exception exception = new Exception();

        ResultObject excepted = new ResultObject(false, null, exception);
        ResultObject actual = new ResultObject(true).setError(exception);

        Assert.assertTrue(new ReflectionEquals(excepted).matches(actual));
    }

    @Test
    public void setErrorMethod (){
        Exception exception = new Exception();

        ResultObject excepted = new ResultObject(false, null, exception);

        ResultObject resultObject = new ResultObject().setError(exception);

        Assert.assertTrue(new ReflectionEquals(excepted).matches(resultObject));
    }

    @Test
    public void setErrorMethodWithMessage(){
        Exception exception = new Exception();

        ResultObject excepted = new ResultObject(false, "Naber", exception);
        ResultObject actual = new ResultObject().setError(exception, "Naber");

        Assert.assertTrue(new ReflectionEquals(excepted).matches(actual));
    }

    @Test
    public void setDataMethod(){
        ResultObject excepted = new ResultObject(true, null, "TEST");
        ResultObject actual = new ResultObject(true).setData("TEST");

        Assert.assertTrue(new ReflectionEquals(excepted).matches(actual));
    }
}
