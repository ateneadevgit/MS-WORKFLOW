package com.fusm.workflow.entity;

import lombok.*;

import javax.persistence.*;

@Builder
@Entity
@Data
@Table(name = "Syllabus_complementary_information")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SyllabusComplementaryInformation {

    @Id
    @Column(name =  "id_complementary_information", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer complementaryInformationId;

    @NonNull
    @Column(name = "subject_conformation", length = 1000, nullable = false)
    private String subjectConformation;

    @NonNull
    @Column(name = "subject_context", length = 1000, nullable = false)
    private String subjectContext;

    @NonNull
    @Column(name = "subject_description", length = 1000, nullable = false)
    private String subjectDescription;

    @NonNull
    @Column(name = "learning_general", length = 1000, nullable = false)
    private String learningGeneral;

    @NonNull
    @Column(name = "learning_specific", length = 1000, nullable = false)
    private String learningSpecific;

    @NonNull
    @Column(name = "content", length = 1000, nullable = false)
    private String content;

    @NonNull
    @Column(name = "pedagogical_practices", length = 1000, nullable = false)
    private String pedagogicalPractices;

    @NonNull
    @Column(name = "bibliography_basic", length = 1000, nullable = false)
    private String bibliographyBasic;

    @NonNull
    @Column(name = "bibliography_lenguaje", length = 1000, nullable = false)
    private String bibliographyLenguaje;

    @NonNull
    @Column(name = "bibliography_web", length = 1000, nullable = false)
    private String bibliographyWeb;

}
