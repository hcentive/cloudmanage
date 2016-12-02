package com.hcentive.cloudmanage.service.provider.aws;


import com.amazonaws.services.ec2.AmazonEC2Client;
import com.amazonaws.services.ec2.model.Tag;
import com.hcentive.cloudmanage.ApplicationTests;
import com.hcentive.cloudmanage.domain.AWSClientProxy;
import com.hcentive.cloudmanage.domain.Instance;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class EC2ServiceImplTest extends ApplicationTests{

    @InjectMocks private EC2Service ec2Service = new EC2ServiceImpl();
    @Mock private AmazonEC2Client amazonEC2Client;
    @Mock private AWSClientProxy awsClientProxy;

    private final String TAG_KEY = "UnitTestTagKey";
    private final String TAG_VALUE = "UnitTestTagValue";
    private final String RESOURCE = "UnitTestResource";

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
        when(awsClientProxy.getEC2Client(anyBoolean(),any(HashMap.class))).thenReturn(amazonEC2Client);
    }

    @Test
    public void testCreateTagSuccess(){
        ec2Service.createTag(TAG_KEY,TAG_VALUE,RESOURCE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateTagNullTagKey(){
        ec2Service.createTag(null,TAG_VALUE,RESOURCE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateTagEmptyTagKey(){
        ec2Service.createTag("",TAG_VALUE,RESOURCE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateTagNullTagValue(){
        ec2Service.createTag(TAG_KEY,null,RESOURCE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateTagEmptyTagValue(){
        ec2Service.createTag(TAG_KEY,"",RESOURCE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateTagNullResource(){
        ec2Service.createTag(TAG_KEY,TAG_VALUE,null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateTagEmptyResource(){
        ec2Service.createTag(TAG_KEY,TAG_VALUE,"");
    }

    @Test
    public void testDeleteTagSuccess(){
        ec2Service.deleteTag(TAG_KEY,RESOURCE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeleteTagNullTagKey(){
        ec2Service.deleteTag(null,RESOURCE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeleteTagEmptyTagKey(){
        ec2Service.deleteTag("",RESOURCE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeleteTagNullResource(){
        ec2Service.deleteTag(TAG_KEY,null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeleteTagEmptyResource(){
        ec2Service.deleteTag(TAG_KEY,"");
    }

    @Test
    public void testIsTagPresentTrue(){
        Instance instance = new Instance(new com.amazonaws.services.ec2.model.Instance());
        List<Tag> tags = new ArrayList<>();
        tags.add(new Tag().withKey(TAG_KEY).withValue(TAG_VALUE));
        instance.getAwsInstance().setTags(tags);
        assertEquals(ec2Service.isTagPresent(instance,TAG_KEY),true);
    }

    @Test
    public void testIsTagPresentFalse(){
        Instance instance = new Instance(new com.amazonaws.services.ec2.model.Instance());
        List<Tag> tags = new ArrayList<>();
        tags.add(new Tag().withKey(TAG_KEY).withValue(TAG_VALUE));
        instance.getAwsInstance().setTags(tags);
        assertEquals(ec2Service.isTagPresent(instance,TAG_KEY + "NotPresent"),false);
    }

    @Test
    public void testIsTagPresentWithSetTrue(){
        Instance instance = new Instance(new com.amazonaws.services.ec2.model.Instance());
        List<Tag> tags = new ArrayList<>();
        Set<String> tagSet = new HashSet<>();
        tags.add(new Tag().withKey(TAG_KEY).withValue(TAG_VALUE));
        instance.getAwsInstance().setTags(tags);
        tagSet.add(TAG_VALUE.toLowerCase());
        assertEquals(ec2Service.isTagPresent(instance,TAG_KEY,tagSet),true);
    }

    @Test
    public void testIsTagPresentWithSetFalse(){
        Instance instance = new Instance(new com.amazonaws.services.ec2.model.Instance());
        List<Tag> tags = new ArrayList<>();
        Set<String> tagSet = new HashSet<>();
        tags.add(new Tag().withKey(TAG_KEY).withValue(TAG_VALUE));
        instance.getAwsInstance().setTags(tags);
        tagSet.add(TAG_VALUE);
        assertEquals(ec2Service.isTagPresent(instance,TAG_KEY,tagSet),false);
    }
}
