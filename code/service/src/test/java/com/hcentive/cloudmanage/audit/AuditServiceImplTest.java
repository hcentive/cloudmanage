package com.hcentive.cloudmanage.audit;


import com.hcentive.cloudmanage.ApplicationTests;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

public class AuditServiceImplTest extends ApplicationTests{

    @InjectMocks AuditService auditService = new AuditServiceImpl();
    @Mock AuditRepository auditRepository;

    private Page<AuditEntity> auditEntityPage;
    private List<AuditEntity> auditEntities;

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
        auditEntityPage = mock(Page.class);
        when(auditRepository.countByUserName(anyString())).thenReturn(Long.valueOf(auditEntities().size()));
        when(auditEntityPage.getContent()).thenReturn(auditEntities());
        when(auditRepository.findByUserName(anyString(),any(PageRequest.class))).thenReturn(auditEntityPage);
    }

    @Test
    public void testCountByUserNameSuccess(){
        when(auditRepository.countByUserName(anyString())).thenReturn(Long.valueOf(auditEntities().size()));
        Long auditEntityCount = auditService.countByUserName("UnitTestUser");
        assertEquals(auditEntityCount,Long.valueOf(3));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCountByUserNameNull(){
        when(auditRepository.countByUserName(anyString())).thenReturn(Long.valueOf(auditEntities().size()));
        Long auditEntityCount = auditService.countByUserName(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCountByUserNameEmpty(){
        when(auditRepository.countByUserName(anyString())).thenReturn(Long.valueOf(auditEntities().size()));
        Long auditEntityCount = auditService.countByUserName("");
    }

    @Test
    public void testGetAuditsWithRecentAddedSuccess(){
        auditContextHolder();
        auditEntities = auditService.getAudits(0,3,true);
        assertEquals(auditEntities.get(0).getEventType(),"UnitTestDeleted");
        assertEquals(auditEntities.get(1).getEventType(),"UnitTestUpdated");
        assertEquals(auditEntities.get(2).getEventType(),"UnitTestCreated");
    }

    @Test
    public void testGetAuditsWithoutRecentAddedSuccess(){
        auditContextHolder();
        auditEntities = auditService.getAudits(0,3,false);
        assertEquals(auditEntities.get(0).getEventType(),"UnitTestCreated");
        assertEquals(auditEntities.get(1).getEventType(),"UnitTestUpdated");
        assertEquals(auditEntities.get(2).getEventType(),"UnitTestDeleted");
    }

    @Test
    public void testGetAuditsWithLatestNull(){
        auditContextHolder();
        auditEntities = auditService.getAudits(0,3,null);
        assertEquals(auditEntities.get(0).getEventType(),"UnitTestDeleted");
        assertEquals(auditEntities.get(1).getEventType(),"UnitTestUpdated");
        assertEquals(auditEntities.get(2).getEventType(),"UnitTestCreated");
    }

    @Test
    public void testGetAuditsWithPageSegmentNegative(){
        auditContextHolder();
        auditEntities = auditService.getAudits(-10,3,null);
        assertEquals(auditEntities.get(0).getEventType(),"UnitTestDeleted");
        assertEquals(auditEntities.get(1).getEventType(),"UnitTestUpdated");
        assertEquals(auditEntities.get(2).getEventType(),"UnitTestCreated");
    }

    @Test(expected = NullPointerException.class)
    public void testGetAuditsNullPointerException(){
        AuditContextHolder.setContext(null);
        auditEntities = auditService.getAudits(-10,3,null);
    }

    private List<AuditEntity> auditEntities(){
        List<AuditEntity> auditEntities = new ArrayList<>();
        auditEntities.add(new AuditEntity("UnitTestCreated","UnitTestEventArgs","UnitTestUser"));
        auditEntities.add(new AuditEntity("UnitTestUpdated","UnitTestEventArgs","UnitTestUser"));
        auditEntities.add(new AuditEntity("UnitTestDeleted","UnitTestEventArgs","UnitTestUser"));
        return auditEntities;
    }

    private void auditContextHolder(){
        AuditContext auditContext = new AuditContext();
        auditContext.setInitiator("UnitTestInitiator");
        AuditContextHolder.setContext(auditContext);
    }
}
