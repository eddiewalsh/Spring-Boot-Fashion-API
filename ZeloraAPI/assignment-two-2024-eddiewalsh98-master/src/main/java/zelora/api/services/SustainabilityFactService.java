package zelora.api.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zelora.api.model.SustainabilityFacts;
import zelora.api.repository.SustainabilityFactRepository;

import java.util.Optional;
import java.util.Random;

@Service
public class SustainabilityFactService {

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private SustainabilityFactRepository sustainabilityFactRepository;

    public Optional<SustainabilityFacts> getFactByID(Integer fact_id){
        return sustainabilityFactRepository.findById(fact_id);
    }

    public SustainabilityFacts generateRandomFact() {
        Random random = new Random();
        Integer randomIndex = random.nextInt(25);

        Optional<SustainabilityFacts> fact = getFactByID(randomIndex);
        return fact.orElse(null);
    }
}
