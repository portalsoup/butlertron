import com.portalsoup.mrbutlertron.v2.core.reply
import com.portalsoup.mrbutlertron.v2.dsl.command
import com.portalsoup.mrbutlertron.v2.dsl.embed

val embedTest = command {
    name = "Embedded message api test"
    command = "embed"
    description = "Displays a test embedded message"

    job("Display a test embed") {

        help {
            description = ""
            trigger = ""
            example { "" }
        }

        action { event ->
            embed {
                author {
                    name = "Author is me"
                    url = "https://portalsoup.com"
                }

                description = "I am a description hooarst as astarstarstarstarstasars tarst arst arst ars tarst  haa"

                footer {
                    text = "I'm the footer"
                    iconUrl = "https://c1.scryfall.com/file/scryfall-cards/normal/front/7/d/7de1dfcf-f2e7-4c7c-8b00-c5d30a2d3f98.jpg?1562921518"
                }

                image {
                    url = "https://dodo.ac/np/images/2/2b/Bam_NH.png"
                }

                thumbnail {
                    url = "https://c1.scryfall.com/file/scryfall-cards/normal/front/7/d/7de1dfcf-f2e7-4c7c-8b00-c5d30a2d3f98.jpg?1562921518"
                }

                field {
                    name = "test1"
                    value = "test1test1test1"
                    inline = true
                }

                field {
                    name = "test2"
                    value = "test2test2test2"
                    inline = true
                }

                field {
                    name = "test3"
                    value = "test3test3test3test3test3test3test3test3test3"
                    inline = false
                }

                field {
                    name = "test4"
                    value = "test4test4test4test4test4test4test4test4"
                    inline = false
                }
            }.also {
                event.reply(it)
            }
        }
    }
}