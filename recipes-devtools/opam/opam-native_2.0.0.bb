DESCRIPTION = "Package Manager for OCaml"
SECTION = "devel"
LICENSE = "GPLv2"

SRC_URI = " \
    https://github.com/ocaml/opam/archive/${PV}.tar.gz \
    file://0001-ocamlrun-pathfix.patch \
    file://0002-jbuilder-pathfix.patch \
    "

LIC_FILES_CHKSUM = "file://LICENSE;md5=d9e77cf0b09010013d038358f983b42a"

SRC_URI[md5sum] = "12b9835e43ff023a0b04a922f0ed9db5"
SRC_URI[sha256sum] = "1e9b47a66a952571b32217c58d0c58d2d66706083969dcfa1e42a60ecce30121"

DEPENDS += " ocaml-native bzip2-native"

inherit autotools-brokensep native

#PARALLEL_MAKE = "-j 1"

do_compile_prepend () {
    oe_runmake lib-ext
}
