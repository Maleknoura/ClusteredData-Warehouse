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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;




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
    @DisplayName("Given valid request when save then should return response successfully")
    void givenValidRequest_whenSave_thenShouldReturnResponseSuccessfully() {

        when(fxDealRepository.existsById("DEAL123")).thenReturn(false);
        when(fxDealMapper.toEntity(requestDto)).thenReturn(fxDeal);
        when(fxDealRepository.save(fxDeal)).thenReturn(fxDeal);
        when(fxDealMapper.toResponseDto(fxDeal)).thenReturn(responseDto);


        FxDealResponseDto result = fxDealService.save(requestDto);


        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo("DEAL123");
        assertThat(result.fromCurrency()).isEqualTo("USD");
        assertThat(result.toCurrency()).isEqualTo("EUR");

        verify(currencyVerifier).validate("USD", "EUR");
        verify(fxDealRepository).existsById("DEAL123");
        verify(fxDealMapper).toEntity(requestDto);
        verify(fxDealRepository).save(fxDeal);
        verify(fxDealMapper).toResponseDto(fxDeal);
    }

    @Test
    @DisplayName("Given invalid currency when save then should throw InvalidCurrencyException")
    void givenInvalidCurrency_whenSave_thenShouldThrowInvalidCurrencyException() {

        doThrow(new InvalidCurrencyException("Invalid currency")).when(currencyVerifier)
                .validate("USD", "EUR");

        assertThatExceptionOfType(InvalidCurrencyException.class)
                .isThrownBy(() -> fxDealService.save(requestDto))
                .withMessage("Invalid currency");

        verify(currencyVerifier).validate("USD", "EUR");
        verify(fxDealRepository, never()).existsById(anyString());
        verify(fxDealMapper, never()).toEntity(any());
        verify(fxDealRepository, never()).save(any());
        verify(fxDealMapper, never()).toResponseDto(any());
    }

    @Test
    @DisplayName("Given duplicate deal ID when save then should throw DuplicateDealException")
    void givenDuplicateDealId_whenSave_thenShouldThrowDuplicateDealException() {

        when(fxDealRepository.existsById("DEAL123")).thenReturn(true);


        assertThatExceptionOfType(DuplicateDealException.class)
                .isThrownBy(() -> fxDealService.save(requestDto))
                .withMessage("A deal with ID 'DEAL123' already exists.");

        verify(currencyVerifier).validate("USD", "EUR");
        verify(fxDealRepository).existsById("DEAL123");
        verify(fxDealMapper, never()).toEntity(any());
        verify(fxDealRepository, never()).save(any());
        verify(fxDealMapper, never()).toResponseDto(any());
    }

    @Test
    @DisplayName("Given repository exception during save when save then should throw RuntimeException")
    void givenRepositoryExceptionDuringSave_whenSave_thenShouldThrowRuntimeException() {

        when(fxDealRepository.existsById("DEAL123")).thenReturn(false);
        when(fxDealMapper.toEntity(requestDto)).thenReturn(fxDeal);
        when(fxDealRepository.save(fxDeal)).thenThrow(new RuntimeException("Database error"));


        assertThatExceptionOfType(RuntimeException.class)
                .isThrownBy(() -> fxDealService.save(requestDto))
                .withMessage("Database error");

        verify(currencyVerifier).validate("USD", "EUR");
        verify(fxDealRepository).existsById("DEAL123");
        verify(fxDealMapper).toEntity(requestDto);
        verify(fxDealRepository).save(fxDeal);
        verify(fxDealMapper, never()).toResponseDto(any());
    }

    @Test
    @DisplayName("Given mapper exception during entity conversion when save then should throw RuntimeException")
    void givenMapperExceptionDuringEntityConversion_whenSave_thenShouldThrowRuntimeException() {

        when(fxDealRepository.existsById("DEAL123")).thenReturn(false);
        when(fxDealMapper.toEntity(requestDto)).thenThrow(new RuntimeException("Mapping error"));


        assertThatExceptionOfType(RuntimeException.class)
                .isThrownBy(() -> fxDealService.save(requestDto))
                .withMessage("Mapping error");

        verify(currencyVerifier).validate("USD", "EUR");
        verify(fxDealRepository).existsById("DEAL123");
        verify(fxDealMapper).toEntity(requestDto);
        verify(fxDealRepository, never()).save(any());
        verify(fxDealMapper, never()).toResponseDto(any());
    }

    @Test
    @DisplayName("Given mapper exception during response conversion when save then should throw RuntimeException")
    void givenMapperExceptionDuringResponseConversion_whenSave_thenShouldThrowRuntimeException() {

        when(fxDealRepository.existsById("DEAL123")).thenReturn(false);
        when(fxDealMapper.toEntity(requestDto)).thenReturn(fxDeal);
        when(fxDealRepository.save(fxDeal)).thenReturn(fxDeal);
        when(fxDealMapper.toResponseDto(fxDeal)).thenThrow(new RuntimeException("Response mapping error"));


        assertThatExceptionOfType(RuntimeException.class)
                .isThrownBy(() -> fxDealService.save(requestDto))
                .withMessage("Response mapping error");

        verify(currencyVerifier).validate("USD", "EUR");
        verify(fxDealRepository).existsById("DEAL123");
        verify(fxDealMapper).toEntity(requestDto);
        verify(fxDealRepository).save(fxDeal);
        verify(fxDealMapper).toResponseDto(fxDeal);
    }

    @Test
    @DisplayName("Given all dependencies when constructor called then should create instance successfully")
    void givenAllDependencies_whenConstructorCalled_thenShouldCreateInstanceSuccessfully() {

        FxDealServiceImpl service = new FxDealServiceImpl(fxDealRepository, fxDealMapper, currencyVerifier);


        assertThat(service).isNotNull();
    }

}