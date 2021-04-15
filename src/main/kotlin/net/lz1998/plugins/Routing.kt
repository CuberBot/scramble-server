package net.lz1998.plugins

import io.ktor.routing.*
import io.ktor.http.*
import io.ktor.application.*
import io.ktor.response.*
import net.lz1998.puzzle.Puzzles
import org.apache.batik.anim.dom.SVGDOMImplementation
import org.apache.batik.transcoder.TranscoderInput
import org.apache.batik.transcoder.TranscoderOutput
import org.apache.batik.transcoder.TranscodingHints
import org.apache.batik.transcoder.image.ImageTranscoder
import org.apache.batik.transcoder.image.PNGTranscoder
import org.apache.batik.util.SVGConstants
import java.io.ByteArrayOutputStream

val puzzles = Puzzles.values().associateBy { it.shortName }.toSortedMap()

fun Application.configureRouting() {

    routing {
        get("/") {
            call.respondText("""
                Usage:
                /scramble/<TYPE>
                /view/<TYPE>?scramble=<SCRAMBLE>&format=<PNG|SVG>
                
                TYPE:
                ${puzzles.keys.joinToString(", ")}
                
                Example:
                /scramble/333
                /scramble/444
                /scramble/sq1
                /view/333?scramble=R%20U&format=png
                /view/pyram?scramble=R%20U&format=svg
            """.trimIndent())
        }

        route("/scramble") {
            log.info("configuring scramble")
            puzzles.forEach {
                val puzzle = it.value
                get(puzzle.shortName) {
                    call.respondText(puzzle.generateScramble())
                }
            }
        }
        route("/view") {
            log.info("configuring view")
            puzzles.forEach {
                val puzzle = it.value.puzzle
                get(puzzle.shortName) {
                    val scramble = call.request.queryParameters["scramble"]
                    val format = call.request.queryParameters["format"] ?: "svg"
                    log.info("view $format ${puzzle.shortName} $scramble")
                    val svg = puzzle.drawScramble(scramble, puzzle.defaultColorScheme)
                    when (format.toLowerCase()) {
                        "png" -> {
                            val (width, height) = puzzle.preferredSize.let { it.width to it.height }
                            val hints = TranscodingHints().apply {
                                this[ImageTranscoder.KEY_WIDTH] = width.toFloat()
                                this[ImageTranscoder.KEY_HEIGHT] = height.toFloat()
                                this[ImageTranscoder.KEY_DOM_IMPLEMENTATION] =
                                    SVGDOMImplementation.getDOMImplementation()
                                this[ImageTranscoder.KEY_DOCUMENT_ELEMENT_NAMESPACE_URI] =
                                    SVGConstants.SVG_NAMESPACE_URI
                                this[ImageTranscoder.KEY_DOCUMENT_ELEMENT_NAMESPACE_URI] =
                                    SVGConstants.SVG_NAMESPACE_URI
                                this[ImageTranscoder.KEY_DOCUMENT_ELEMENT] = SVGConstants.SVG_SVG_TAG
                                this[ImageTranscoder.KEY_XML_PARSER_VALIDATING] = false
                            }

                            val imageTranscoder = PNGTranscoder().apply {
                                transcodingHints = hints
                            }

                            val input = TranscoderInput(svg.toString().byteInputStream())

                            val outputBytes = ByteArrayOutputStream()
                            val output = TranscoderOutput(outputBytes)
                            imageTranscoder.transcode(input, output)

                            call.respondBytes(outputBytes.toByteArray(), ContentType.Image.PNG)
                        }
                        else -> {
                            call.respondText(svg.toString(), ContentType.Image.SVG)
                        }
                    }
                }
            }
        }
    }
}
