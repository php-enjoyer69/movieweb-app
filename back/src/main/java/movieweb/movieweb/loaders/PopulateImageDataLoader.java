package movieweb.movieweb.loaders;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import movieweb.movieweb.entities.Image;
import movieweb.movieweb.repositories.ImageRepository;
import movieweb.movieweb.services.ImageService;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class PopulateImageDataLoader implements ApplicationRunner
{
  @Value("classpath:movie1.jpg")
  private Resource resource;

  private final ImageRepository imageRepository;
  private final ImageService imageService;

  public void run(ApplicationArguments args)
  {
    Optional<Image> optionalImage = imageRepository.findByName(resource.getFilename());

    if (optionalImage.isPresent())
      return;

    try (InputStream inputStream = resource.getInputStream()) {
      byte[] imageBytes = inputStream.readAllBytes();

      imageRepository.save(
          Image.builder()
              .name(resource.getFilename())
              .type(MediaType.IMAGE_JPEG_VALUE)
              .imageBlob(imageService.compress(imageBytes))
              .build()
      );

    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
