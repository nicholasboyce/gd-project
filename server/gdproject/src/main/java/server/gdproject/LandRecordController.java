package server.gdproject;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;
import java.net.URI;

import java.util.Optional;
import java.util.List;

@RestController
@RequestMapping("/landrecords")
class LandRecordController {

    private LandRecordRepository landRecordRepository;

    private LandRecordController(LandRecordRepository landRecordRepository) {
        this.landRecordRepository = landRecordRepository;
    }

    @GetMapping
    private ResponseEntity<List<LandRecord>> findAll(Pageable pageable) {
        Page<LandRecord> page = landRecordRepository.findAll(
            PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                pageable.getSort()
            )
        );

        return ResponseEntity.ok(page.getContent());
    } 
    
    @GetMapping("/{requestedId}")
    private ResponseEntity<LandRecord> findById(@PathVariable Long requestedId) {
        Optional<LandRecord> landRecordOptional = landRecordRepository.findById(requestedId);

        if (landRecordOptional.isPresent()) {
            return ResponseEntity.ok(landRecordOptional.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    private ResponseEntity<Void> createLandRecord(@RequestBody LandRecord newLandRecordRequest, UriComponentsBuilder ucb) {
        LandRecord savedLandRecord = landRecordRepository.save(newLandRecordRequest);

        URI locationOfNewLandRecord = ucb
                                        .path("/landrecords/{id}")
                                        .buildAndExpand(savedLandRecord.id())
                                        .toUri();
        
        return ResponseEntity.created(locationOfNewLandRecord).build();
    }
    
}
