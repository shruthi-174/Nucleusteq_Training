package com.emp.repository;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.emp.entities.RequestResource;
import com.emp.entities.RequestResource.RequestStatus;

public class RequestResourceRepositoryTests {

    @Mock
    private RequestResourceRepository requestResourceRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindAll() {
        RequestResource requestResource = new RequestResource();
        requestResource.setRequestId(1L);
        requestResource.setStatus(RequestStatus.PENDING);

        when(requestResourceRepository.findAll()).thenReturn(Arrays.asList(requestResource));

        List<RequestResource> requestResources = requestResourceRepository.findAll();

        assertNotNull(requestResources);
        assertEquals(1, requestResources.size());
        assertEquals(1L, requestResources.get(0).getRequestId());
        assertEquals(RequestStatus.PENDING, requestResources.get(0).getStatus());
    }

    @Test
    public void testFindById() {
        RequestResource requestResource = new RequestResource();
        requestResource.setRequestId(1L);
        requestResource.setStatus(RequestStatus.PENDING);

        when(requestResourceRepository.findById(1L)).thenReturn(Optional.of(requestResource));

        Optional<RequestResource> foundRequestResource = requestResourceRepository.findById(1L);

        assertTrue(foundRequestResource.isPresent());
        assertEquals(1L, foundRequestResource.get().getRequestId());
        assertEquals(RequestStatus.PENDING, foundRequestResource.get().getStatus());
    }

    @Test
    public void testSave() {
        RequestResource requestResource = new RequestResource();
        requestResource.setRequestId(1L);
        requestResource.setStatus(RequestStatus.PENDING);

        when(requestResourceRepository.save(requestResource)).thenReturn(requestResource);

        RequestResource savedRequestResource = requestResourceRepository.save(requestResource);

        assertNotNull(savedRequestResource);
        assertEquals(1L, savedRequestResource.getRequestId());
        assertEquals(RequestStatus.PENDING, savedRequestResource.getStatus());
    }
}
