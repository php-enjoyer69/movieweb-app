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
  @Value("classpath:movie*.jpg")
  private Resource[] movieResources;

  @Value("classpath:person*.jpg")
  private Resource[] personResources;

  @Value("classpath:avatar.jpg")
  private Resource avatarResource;

  private final ImageRepository imageRepository;
  private final ImageService imageService;

  @Override
  public void run(ApplicationArguments args)
  {
    loadImages(movieResources);
    loadImages(personResources);
    loadAvatarImage();
  }

  private void loadImages(Resource[] resources) {
    for (Resource resource : resources) {
      saveImageIfNotExists(resource);
    }
  }

  private void loadAvatarImage() {
    if (avatarResource.exists()) {
      saveImageIfNotExists(avatarResource);
    }
  }

  private void saveImageIfNotExists(Resource resource) {
    try {
      Optional<Image> optionalImage = imageRepository.findByName(resource.getFilename());
      if (optionalImage.isPresent()) return;

      try (InputStream inputStream = resource.getInputStream()) {
        byte[] imageBytes = inputStream.readAllBytes();

        imageRepository.save(
                Image.builder()
                        .name(resource.getFilename())
                        .type(MediaType.IMAGE_JPEG_VALUE)
                        .imageBlob(imageService.compress(imageBytes))
                        .build()
        );
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
