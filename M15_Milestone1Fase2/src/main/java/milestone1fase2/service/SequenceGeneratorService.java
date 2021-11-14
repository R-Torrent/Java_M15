package milestone1fase2.service;

import java.util.Objects;

import static org.springframework.data.mongodb.core.FindAndModifyOptions.options;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import milestone1fase2.domain.DatabaseSequence;

@Service
public class SequenceGeneratorService {
	
	@Autowired
	private MongoOperations mongoOperations;
	
	public long generateSequence(String seqName) {
		// Obtener secuencia
		Query query = new Query(Criteria.where("_id").is(seqName));
		// Actualizar el número de la secuencia
		Update update = new Update().inc("seq", 1);
		// Modificar el documento
		DatabaseSequence counter = mongoOperations.findAndModify(query, update,
				options().returnNew(true).upsert(true), DatabaseSequence.class);
		
		return !Objects.isNull(counter) ? counter.getSeq() : 1;
	}
	
}
