package cl.duoc.inscripcion;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import cl.duoc.inscripcion.entities.Curso;
import cl.duoc.inscripcion.repositories.CursoRepository;

@Configuration
public class DataLoader {

    @Bean
    public CommandLineRunner cargarDatos(CursoRepository cursoRepository) {
        return args -> {
            if (cursoRepository.count() == 0) {
                cursoRepository.save(new Curso("Introduccion a Java",        "Carlos Valverde",  40, 120000.0));
                cursoRepository.save(new Curso("Spring Boot Avanzado",       "Alonso Castillo",  60, 180000.0));
                cursoRepository.save(new Curso("Docker y Contenedores",      "Carlos Valverde",  30,  90000.0));
                cursoRepository.save(new Curso("AWS para Desarrolladores",   "Alonso Castillo",  50, 150000.0));
                cursoRepository.save(new Curso("CI/CD con GitHub Actions",   "Carlos Valverde",  25,  80000.0));
                System.out.println("Cursos iniciales cargados en la base de datos");
            }
        };
    }
}
