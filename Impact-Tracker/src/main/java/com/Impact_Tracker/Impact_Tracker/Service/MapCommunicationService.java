package com.Impact_Tracker.Impact_Tracker.Service;

import com.Impact_Tracker.Impact_Tracker.DTO.MapCommunicationDto;
import com.Impact_Tracker.Impact_Tracker.Entity.MapCommunication;
import com.Impact_Tracker.Impact_Tracker.Repo.MapCommunicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MapCommunicationService {

    @Autowired
    private MapCommunicationRepository repository;

    public MapCommunicationDto create(MapCommunicationDto dto) {
        MapCommunication entity = new MapCommunication();
        entity.setName(dto.getName());
        entity.setAddress(dto.getAddress());
        entity.setGooglePlacesLink(dto.getGooglePlacesLink());
        entity.setBusinessType(dto.getBusinessType());
        MapCommunication saved = repository.save(entity);
        return mapToDto(saved);
    }

    public MapCommunicationDto getById(Long id) {
        MapCommunication entity = repository.findById(id)
            .orElseThrow(() -> new RuntimeException("Not found with ID: " + id));
        return mapToDto(entity);
    }

    public List<MapCommunicationDto> getAll() {
        return repository.findAll().stream().map(this::mapToDto).collect(Collectors.toList());
    }

    public MapCommunicationDto update(Long id, MapCommunicationDto dto) {
        MapCommunication entity = repository.findById(id)
            .orElseThrow(() -> new RuntimeException("Not found with ID: " + id));
        entity.setName(dto.getName());
        entity.setAddress(dto.getAddress());
        entity.setGooglePlacesLink(dto.getGooglePlacesLink());
        entity.setBusinessType(dto.getBusinessType());
        MapCommunication updated = repository.save(entity);
        return mapToDto(updated);
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Not found with ID: " + id);
        }
        repository.deleteById(id);
    }

    private MapCommunicationDto mapToDto(MapCommunication entity) {
        MapCommunicationDto dto = new MapCommunicationDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setAddress(entity.getAddress());
        dto.setGooglePlacesLink(entity.getGooglePlacesLink());
        dto.setBusinessType(entity.getBusinessType());
        return dto;
    }
}
