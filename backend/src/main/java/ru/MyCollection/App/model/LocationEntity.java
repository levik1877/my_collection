package ru.MyCollection.App.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "location")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LocationEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "country_id",
            nullable = false,
            foreignKey = @ForeignKey(
                    name = "fk_location_country",
                    options = ForeignKeyOptions.CASCADE_UPDATE_RESTRICT_DELETE
            )
    )
    private CountryEntity country;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "city_id",
            nullable = false,
            foreignKey = @ForeignKey(
                    name = "fk_location_city",
                    options = ForeignKeyOptions.CASCADE_UPDATE_RESTRICT_DELETE
            )
    )
    private CityEntity city;
}