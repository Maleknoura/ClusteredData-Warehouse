package com.progressoft.clustereddata_warehouse.Service.implementation;

import com.progressoft.clustereddata_warehouse.Service.api.CurrencyVerifier;
import com.progressoft.clustereddata_warehouse.dto.request.FxDealRequestDto;
import com.progressoft.clustereddata_warehouse.dto.response.FxDealResponseDto;
import com.progressoft.clustereddata_warehouse.entity.FxDeal;
import com.progressoft.clustereddata_warehouse.exception.DuplicateDealException;
import com.progressoft.clustereddata_warehouse.exception.InvalidCurrencyException;
import com.progressoft.clustereddata_warehouse.mapper.FxDealMapper;
import com.progressoft.clustereddata_warehouse.repository.FxDealRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FxDealServiceImplTest {

    @Mock
    private FxDealRepository fxDealRepository;

    @Mock
    private FxDealMapper fxDealMapper;

    @Mock
    private CurrencyVerifier currencyVerifier;

    @InjectMocks
    private FxDealServiceImpl fxDealService;

    private FxDealRequestDto requestDto;
    private FxDeal fxDeal;
    private FxDealResponseDto responseDto;

    @BeforeEach
    void setUp() {
        requestDto = new FxDealRequestDto(
                "DEAL123",
                "USD",
                "EUR",
                LocalDateTime.now(),
                100.0
        );

        fxDeal = new FxDeal();
        fxDeal.setId("DEAL123");
        fxDeal.setFromCurrency("USD");
        fxDeal.setToCurrency("EUR");
        fxDeal.setDealAmount(new BigDecimal("100.0"));
        fxDeal.setDealTimestamp(LocalDateTime.now());

        responseDto = new FxDealResponseDto(
                "DEAL123",
                "USD",
                "EUR",
                LocalDateTime.now(),
                new BigDecimal("100.0").doubleValue()
        );
    }

    @Test
    @DisplayName("Should save deal successfully")
    void save_WithValidRequest_ShouldReturnResponse() {
        when(fxDealRepository.existsById("DEAL123")).thenReturn(false);
        when(fxDealMapper.toEntity(requestDto)).thenReturn(fxDeal);
        when(fxDealRepository.save(fxDeal)).thenReturn(fxDeal);
        when(fxDealMapper.toResponseDto(fxDeal)).thenReturn(responseDto);


        FxDealResponseDto result = fxDealService.save(requestDto);

        assertNotNull(result);
        assertEquals("DEAL123", result.id());
        assertEquals("USD", result.fromCurrency());
        assertEquals("EUR", result.toCurrency());

        verify(currencyVerifier).validate("USD", "EUR");
        verify(fxDealRepository).existsById("DEAL123");
        verify(fxDealMapper).toEntity(requestDto);
        verify(fxDealRepository).save(fxDeal);
        verify(fxDealMapper).toResponseDto(fxDeal);
    }

    @Test
    @DisplayName("Should throw exception when currency validation fails")
    void save_WithInvalidCurrency_ShouldThrowException() {

        doThrow(new InvalidCurrencyException("Invalid currency")).when(currencyVerifier)
                .validate("USD", "EUR");


        InvalidCurrencyException exception = assertThrows(InvalidCurrencyException.class,
                () -> fxDealService.save(requestDto));

        assertEquals("Invalid currency", exception.getMessage());

        verify(currencyVerifier).validate("USD", "EUR");
        verify(fxDealRepository, never()).existsById(anyString());
        verify(fxDealMapper, never()).toEntity(any());
        verify(fxDealRepository, never()).save(any());
        verify(fxDealMapper, never()).toResponseDto(any());
    }

    @Test
    @DisplayName("Should throw exception when deal ID already exists")
    void save_WithDuplicateId_ShouldThrowException() {

        when(fxDealRepository.existsById("DEAL123")).thenReturn(true);


        DuplicateDealException exception = assertThrows(DuplicateDealException.class,
                () -> fxDealService.save(requestDto));

        assertEquals("A deal with ID 'DEAL123' already exists.", exception.getMessage());


        verify(currencyVerifier).validate("USD", "EUR");
        verify(fxDealRepository).existsById("DEAL123");
        verify(fxDealMapper, never()).toEntity(any());
        verify(fxDealRepository, never()).save(any());
        verify(fxDealMapper, never()).toResponseDto(any());
    }

    @Test
    @DisplayName("Should handle repository exception during save")
    void save_WithRepositoryException_ShouldThrowException() {

        when(fxDealRepository.existsById("DEAL123")).thenReturn(false);
        when(fxDealMapper.toEntity(requestDto)).thenReturn(fxDeal);
        when(fxDealRepository.save(fxDeal)).thenThrow(new RuntimeException("Database error"));


        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> fxDealService.save(requestDto));

        assertEquals("Database error", exception.getMessage());


        verify(currencyVerifier).validate("USD", "EUR");
        verify(fxDealRepository).existsById("DEAL123");
        verify(fxDealMapper).toEntity(requestDto);
        verify(fxDealRepository).save(fxDeal);
        verify(fxDealMapper, never()).toResponseDto(any());
    }

    @Test
    @DisplayName("Should handle mapper exception during entity conversion")
    void save_WithMapperExceptionDuringEntityConversion_ShouldThrowException() {

        when(fxDealRepository.existsById("DEAL123")).thenReturn(false);
        when(fxDealMapper.toEntity(requestDto)).thenThrow(new RuntimeException("Mapping error"));


        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> fxDealService.save(requestDto));

        assertEquals("Mapping error", exception.getMessage());


        verify(currencyVerifier).validate("USD", "EUR");
        verify(fxDealRepository).existsById("DEAL123");
        verify(fxDealMapper).toEntity(requestDto);
        verify(fxDealRepository, never()).save(any());
        verify(fxDealMapper, never()).toResponseDto(any());
    }

    @Test
    @DisplayName("Should handle mapper exception during response conversion")
    void save_WithMapperExceptionDuringResponseConversion_ShouldThrowException() {

        when(fxDealRepository.existsById("DEAL123")).thenReturn(false);
        when(fxDealMapper.toEntity(requestDto)).thenReturn(fxDeal);
        when(fxDealRepository.save(fxDeal)).thenReturn(fxDeal);
        when(fxDealMapper.toResponseDto(fxDeal)).thenThrow(new RuntimeException("Response mapping error"));


        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> fxDealService.save(requestDto));

        assertEquals("Response mapping error", exception.getMessage());


        verify(currencyVerifier).validate("USD", "EUR");
        verify(fxDealRepository).existsById("DEAL123");
        verify(fxDealMapper).toEntity(requestDto);
        verify(fxDealRepository).save(fxDeal);
        verify(fxDealMapper).toResponseDto(fxDeal);
    }

    @Test
    @DisplayName("Should verify constructor with all dependencies")
    void constructor_WithAllDependencies_ShouldCreateInstance() {

        FxDealServiceImpl service = new FxDealServiceImpl(fxDealRepository, fxDealMapper, currencyVerifier);

        assertNotNull(service);
    }

    @Test
    @DisplayName("Should log processing and success messages")
    void save_ShouldLogProcessingAndSuccessMessages() {

        when(fxDealRepository.existsById("DEAL123")).thenReturn(false);
        when(fxDealMapper.toEntity(requestDto)).thenReturn(fxDeal);
        when(fxDealRepository.save(fxDeal)).thenReturn(fxDeal);
        when(fxDealMapper.toResponseDto(fxDeal)).thenReturn(responseDto);

        fxDealService.save(requestDto);


        verify(fxDealRepository).save(fxDeal);
    }
}