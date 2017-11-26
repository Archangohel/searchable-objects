package com.searchable.objects.main;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * @auther Archan on 26/11/17.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestAppConfig.class})
@WebAppConfiguration
public class ClientCodeTest {

    @Autowired
    private ClientCode clientCode;

    @Test
    public void testIndexCreation() {
        // do nothing it should create indexes.
    }

    @Test
    public void testProcessArgsForUpdate() {
        clientCode.processArgsForUpdate(Entity1.newInstance());
    }

    @Test
    public void testprocessArgsAndReturnValueForUpdate() {
        clientCode.processArgsAndReturnValueForUpdate(Entity1.newInstance());
    }
}
