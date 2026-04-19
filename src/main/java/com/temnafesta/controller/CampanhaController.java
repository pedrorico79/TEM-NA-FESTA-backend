package com.temnafesta.controller;


import com.temnafesta.dto.campanha.CampanhaRequestDto;
import com.temnafesta.dto.campanha.CampanhaResponseDto;
import com.temnafesta.mapper.CampanhaMapper;
import com.temnafesta.service.CampanhaService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/campanhas")
public class CampanhaController {

    private final CampanhaService service;

    public CampanhaController(CampanhaService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<CampanhaResponseDto>> listarTudo(){
        List<CampanhaResponseDto> campanhas = CampanhaMapper.toResponseDto(service.findAll());
        if (campanhas.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(campanhas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CampanhaResponseDto> ProcurarPorId(@PathVariable Integer id){
        return ResponseEntity.ok(CampanhaMapper.toResponse(service.findById(id)));
    }

    @PostMapping
    public ResponseEntity<CampanhaResponseDto> create(@RequestBody @Valid CampanhaRequestDto dto){
        CampanhaResponseDto created = CampanhaMapper.toResponse(service.create(CampanhaMapper.toEntityForCreate(dto)));
        URI location = URI.create("/campanhas/" + created.getId());
        return ResponseEntity.created(location).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CampanhaResponseDto> update(@PathVariable Integer id,
                                                      @RequestBody
                                                      @Valid CampanhaRequestDto dto){
        return ResponseEntity.ok(CampanhaMapper.toResponse(service.update(id, CampanhaMapper.toEntityForUpdate(dto))));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete (@PathVariable Integer id){
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/ativa/{ativa}")
    public ResponseEntity<List<CampanhaResponseDto>> findByAtiva (@PathVariable Boolean ativa) {
        List<CampanhaResponseDto> result = CampanhaMapper.toResponseDto(service.findByAtiva(ativa));
        if(result.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(result);
    }
}
