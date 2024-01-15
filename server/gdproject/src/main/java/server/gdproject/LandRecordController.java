package server.gdproject;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/landrecords")
class LandRecordController {

    private LandRecordRepository landRecordRepository;

    private LandRecordController(LandRecordRepository landRecordRepository) {
        this.landRecordRepository = landRecordRepository;
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
}
