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
    public ResponseEntity<List<CampanhaResponseDto>> listarTudo(
            @RequestParam(required = false, defaultValue = "true") Boolean apenasAtivas
    ){
        List<CampanhaResponseDto> campanhas;
        if (apenasAtivas){
            campanhas = CampanhaMapper.toResponseDto(service.listarAtivas());
        } else {
            campanhas = CampanhaMapper.toResponseDto(service.listarTodas());
        }

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

    @PatchMapping("/{id}/desativar")
    public ResponseEntity<Void> desativar(@PathVariable Integer id){
        service.desativar(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/reativar")
    public ResponseEntity<Void> reativar(@PathVariable Integer id){
        service.reativar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/inativas")
    public ResponseEntity<List<CampanhaResponseDto>> listarInativas() {
        List<CampanhaResponseDto> campanhas = CampanhaMapper.toResponseDto(service.listarInativas());
        if(campanhas.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(campanhas);
    }
}
