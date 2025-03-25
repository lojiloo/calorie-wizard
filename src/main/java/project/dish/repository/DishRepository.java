package project.dish.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.dish.model.Dish;

import java.util.List;

public interface DishRepository extends JpaRepository<Dish, Long> {

    List<Dish> findAllByIdIn(List<Long> ids);

}
