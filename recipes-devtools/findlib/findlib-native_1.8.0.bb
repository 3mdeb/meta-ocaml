DESCRIPTION = "Library to find OCaml Packages"
SECTION = "devel"
LICENSE = "QPL"

SRC_URI = " \
    http://download.camlcity.org/download/findlib-${PV}.tar.gz \
    "

LIC_FILES_CHKSUM = "file://LICENSE;md5=a30ace4f9508a47d2c25c45c48af6492"

SRC_URI[md5sum] = "a710c559667672077a93d34eb6a42e5b"
SRC_URI[sha256sum] = "2b7b7d6d65bb0f3f8a2c51c63c02b0bcf1fea4c23513f866140dc7dc24fe27ad"

DEPENDS += " ocaml-native opam-native"

inherit native

do_configure () {
    ./configure \
        -bindir ${D}${bindir} \
        -mandir ${D}${mandir}
}

# uses ocamlc which uses the host system's gcc. This means it also
# uses host system libraries such as libncurses5-dev.
do_compile () {
    oe_runmake all
    oe_runmake opt
}

do_install () {
    oe_runmake install prefix=""
}
