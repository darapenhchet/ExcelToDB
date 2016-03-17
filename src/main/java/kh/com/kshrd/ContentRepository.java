package kh.com.kshrd;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContentRepository extends CrudRepository<SparePartContent,Long>{

	public SparePartContent findByCodeAndDateAndEnglishTitle(String code, String date, String englishTitle);
	
	public SparePartContent findByCode(String code);
}
