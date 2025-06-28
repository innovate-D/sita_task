package org.sita.service;

import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sita.entity.ProductPortfolio;
import org.sita.repository.ProductPortfolioRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

class ProductServiceTest {

    private ProductPortfolioRepository productPortfolioRepository;
    private ProductService productService;
    private CacheManager cacheManager;
    private Cache cache;

    @BeforeEach
    void setUp() {
        productPortfolioRepository = mock(ProductPortfolioRepository.class);
        cacheManager = mock(CacheManager.class);
        cache = mock(Cache.class);
        when(cacheManager.getCache("Product")).thenReturn(cache);
        productService = new ProductService(productPortfolioRepository, cacheManager);
    }

    @Test
    void testRun_saveProductPortfolioList() throws Exception {

        ApplicationArguments args = mock(ApplicationArguments.class);
        productService.run(args);
        verify(productPortfolioRepository, times(1)).saveAll(
                List.of(
                        new ProductPortfolio("Iphone15", 100, 2000),
                        new ProductPortfolio("Iphone16", 100, 3000),
                        new ProductPortfolio("Iphone17", 100, 4000)
                )
        );

        verify(cache).put("Iphone15",2000);
        verify(cache).put("Iphone16",3000);
        verify(cache).put("Iphone17",4000);

    }
}
