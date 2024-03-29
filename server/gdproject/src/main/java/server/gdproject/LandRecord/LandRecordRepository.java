package server.gdproject.LandRecord;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

interface LandRecordRepository extends CrudRepository<LandRecord, Long>, PagingAndSortingRepository<LandRecord, Long> {
}
