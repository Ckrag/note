package com.note

import javax.persistence.*


@Entity
data class Note(@Id
                @GeneratedValue
                var id: Long,
                var title: String,
                var content: String,
                //var category: Category?,
                //var tags: List<Tag>
)

