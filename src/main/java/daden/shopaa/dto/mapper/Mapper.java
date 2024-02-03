package daden.shopaa.dto.mapper;

import org.modelmapper.ModelMapper;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class Mapper {
  private final ModelMapper mapper;
}
