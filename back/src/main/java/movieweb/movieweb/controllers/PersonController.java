package movieweb.movieweb.controllers;

import lombok.RequiredArgsConstructor;
import movieweb.movieweb.dtos.movies.MovieDto;
import movieweb.movieweb.dtos.persons.NewPersonDto;
import movieweb.movieweb.dtos.persons.PatchPersonDto;
import movieweb.movieweb.dtos.persons.PersonDto;
import movieweb.movieweb.dtos.persons.UserVoteStatusDto;
import movieweb.movieweb.entities.Image;
import movieweb.movieweb.entities.Person;
import movieweb.movieweb.entities.PersonMovieRole;
import movieweb.movieweb.enums.VoteType;
import movieweb.movieweb.mappers.PersonMapper;
import movieweb.movieweb.services.ImageService;
import movieweb.movieweb.services.PersonService;
import movieweb.movieweb.services.PersonMovieRoleService;
import movieweb.movieweb.dtos.roles.PersonMovieRoleDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;
import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@RestController
@Validated
public class PersonController {

    private final PersonService personService;
    private final ImageService imageService;
    private final PersonMovieRoleService personMovieRoleService;
    private final PersonMapper personMapper;

    @GetMapping("/person/{id}")
    public PersonDto getPersonWithRoles(@PathVariable Long id) {
        return personService.getPersonWithRoles(id);
    }

    @GetMapping("/persons")
    public ResponseEntity<Page<PersonDto>> getAll(Pageable pageable, @RequestParam(value = "search", required = false) String search) {
        Page<Person> personPage = personService.findAll(pageable, search);
        Page<PersonDto> personDtoPage = personPage.map(personMapper::toPersonDto);
        return ResponseEntity.ok(personDtoPage);
    }


    @GetMapping("/person/{id}/roles")
    public ResponseEntity<List<PersonMovieRole>> getRolesByPersonId(@PathVariable Long id) {
        List<PersonMovieRole> roles = personMovieRoleService.getRolesByPersonId(id);
        if (roles.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(roles);
        }
        return ResponseEntity.ok(roles);
    }

    @PostMapping("/person")
    public ResponseEntity<PersonDto> add(
            @RequestPart("person") @Valid NewPersonDto newPersonDto,
            @RequestPart("image") MultipartFile image
    ) throws IOException {
        Image savedImage = imageService.upload(image);
        newPersonDto.setImg(savedImage.getName());
        PersonDto personDto = personService.save(newPersonDto);
        return ResponseEntity.ok(personDto);
    }

    @PatchMapping("/person/{id}")
    public ResponseEntity<PersonDto> update(
            @RequestPart(value = "person", required = false) @Valid PatchPersonDto patchPersonDto,
            @RequestPart(value = "image", required = false) MultipartFile patchImage,
            @PathVariable Long id) throws IOException {

        String imageName = null;
        if (patchImage != null) {
            PersonDto personDto = personService.findById(id);
            Image updatedImage = imageService.update(personDto.getImg(), patchImage);
            imageName = updatedImage.getName();
        }

        PersonDto updatedPersonDto = personService.update(patchPersonDto, imageName, id);
        return ResponseEntity.ok(updatedPersonDto);
    }

    @DeleteMapping("/person/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        personService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/person/{id}/vote")
    public ResponseEntity<Void> vote(
            @PathVariable Long id,
            @RequestParam("userId") Long userId,
            @RequestParam("voteType") VoteType voteType) {
        personService.vote(id, userId, voteType);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/persons/voted-by/{userId}")
    public ResponseEntity<List<PersonDto>> getPersonsVotedByUser(@PathVariable Long userId) {
        List<PersonDto> persons = personService.findPersonsVotedByUser(userId);
        return ResponseEntity.ok(persons);
    }

    @GetMapping("/person/{personId}/vote/{userId}")
    public ResponseEntity<UserVoteStatusDto> checkUserVote(
            @PathVariable Long personId,
            @PathVariable Long userId) {

        UserVoteStatusDto voteStatus = personService.getUserVoteStatus(personId, userId);
        return ResponseEntity.ok(voteStatus);
    }
}