DESCRIPTION = "A composable build system for OCaml/Reason"
SECTION = "devel"
LICENSE = "MIT"

SRC_URI = " \
    https://github.com/ocaml/dune/archive/${PV}.tar.gz \
    "

LIC_FILES_CHKSUM = "file://LICENSE.md;md5=3b903cfeb2c8b8e55d6912f183cdea5b"

SRC_URI[md5sum] = "93fd8ed458396ee45f406435f38db161"
SRC_URI[sha256sum] = "08e3cb27c1c2fb15ec36bf1fae40e00eb6e23566c9bf5cc78bc8550ed70f4d59"

DEPENDS += " ocaml-native opam-native"

inherit native

do_configure () {
    ./configure --libdir ${libdir}
}

do_compile () {
    oe_runmake release
}

do_install () {
    install -d ${D}${prefix}
    oe_runmake PREFIX=${D}${prefix} install
}
