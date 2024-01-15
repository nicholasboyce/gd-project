package server.gdproject;

import org.springframework.data.annotation.Id;

public record LandRecord(String address, String owner, int year, int value, int book, @Id int id) {
}
