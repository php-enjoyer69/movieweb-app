package movieweb.movieweb.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import movieweb.movieweb.entities.Image;
import movieweb.movieweb.exceptions.AppException;
import movieweb.movieweb.repositories.ImageRepository;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Optional;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

@RequiredArgsConstructor
@Service
public class ImageService
{
  private final ImageRepository imageRepository;

  public Image upload(MultipartFile file) throws IOException
  {
    Date date = new Date();
    Long time = date.getTime();

    return imageRepository.save(
      Image.builder()
        .name(time + "_" + file.getOriginalFilename())
        .type(file.getContentType())
        .imageBlob(compress(file.getBytes()))
      .build()
    );
  }

  @Transactional
  public byte[] get(String name)
  {
    Optional<Image> optionalImage = imageRepository.findByName(name);
    byte[] image = decompress(optionalImage.get().getImageBlob());

    return image;
  }

  public Image update(String name, MultipartFile file) throws IOException
  {
    Date date = new Date();
    Long time = date.getTime();

    Optional<Image> optionalImage = imageRepository.findByName(name);

    if (!optionalImage.isPresent())
    {
      return imageRepository.save(
        Image.builder()
          .name(time + "_" + file.getOriginalFilename())
          .type(file.getContentType())
          .imageBlob(compress(file.getBytes()))
        .build()
      );
    }

    Image image = optionalImage.get();

    image.setName(time + "_" + file.getOriginalFilename());
    image.setType(file.getContentType());
    image.setImageBlob(compress(file.getBytes()));

    return imageRepository.save(image);
  }

  public void delete(String name)
  {
    Image image = imageRepository.findByName(name)
        .orElseThrow(() -> new AppException("Unknown image", HttpStatus.NOT_FOUND));

    imageRepository.delete(image);
  }

  public byte[] compress(byte[] data) {

    Deflater deflater = new Deflater();
    deflater.setLevel(Deflater.BEST_COMPRESSION);
    deflater.setInput(data);
    deflater.finish();

    ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
    byte[] tmp = new byte[4*1024];

    while (!deflater.finished())
    {
      int size = deflater.deflate(tmp);
      outputStream.write(tmp, 0, size);
    }
    try
    {
      outputStream.close();
    }
    catch (Exception e)
    {

    }

    return outputStream.toByteArray();
  }

  private byte[] decompress(byte[] data)
  {
    Inflater inflater = new Inflater();
    inflater.setInput(data);
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);

    byte[] tmp = new byte[4*1024];

    try
    {
      while (!inflater.finished())
      {
        int count = inflater.inflate(tmp);
        outputStream.write(tmp, 0, count);
      }

      outputStream.close();
    }
    catch (Exception e)
    {

    }

    return outputStream.toByteArray();
  }
}
