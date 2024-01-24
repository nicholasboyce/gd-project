package server.gdproject.LandRecord;

import org.springframework.data.annotation.Id;

public record LandRecord(String address, String owner, int year, int value, int book, @Id Long id) {
}
