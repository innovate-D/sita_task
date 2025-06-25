package org.sita.service;

import lombok.extern.slf4j.Slf4j;
import org.sita.entity.ProductPortfolio;
import org.sita.repository.ProductPortfolioRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ProductService implements ApplicationRunner {

    private final ProductPortfolioRepository productPortfolioRepository;

    public ProductService(ProductPortfolioRepository productPortfolioRepository, CacheManager cacheManager) {
        this.productPortfolioRepository = productPortfolioRepository;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {

        List<ProductPortfolio> list = List.of(
                new ProductPortfolio("Iphone15",100,2000),
                new ProductPortfolio("Iphone16",100,3000),
                new ProductPortfolio("Iphone17",100,4000)
        );

        log.info("Updating product portfolio in db");
        productPortfolioRepository.saveAll(list);

    }
}
