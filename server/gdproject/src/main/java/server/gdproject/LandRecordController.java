package server.gdproject;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/landrecords")
class LandRecordController {
    
    @GetMapping("/{requestedId}")
    private ResponseEntity<LandRecord> findById(@PathVariable Long requestedId) {
        LandRecord landRecord = new LandRecord("123 Mulberry Lane", "Nicholas Boyce", 2024, 777000, 1, 1);
        return ResponseEntity.ok(landRecord);
    }
}
