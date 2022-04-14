package com.note

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
class Tag (@Id
           @GeneratedValue
           var id: Long,
           var title: String,
           var description: String?)