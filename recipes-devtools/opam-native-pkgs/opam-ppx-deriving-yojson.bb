DESCRIPTION = "JSON codec generator for OCaml"
SECTION = "devel"
LICENSE = "MIT"

DEPENDS += " \
    ocaml-native \
    opam-ocamlbuild \
    opam-ppx-deriving \
    opam-result \
    opam-cppo \
    opam-cppo-ocamlbuild \
    "

DEPENDS_remove = " \
    ocaml-cross-${TARGET_ARCH} \
    "

inherit opam_install

# opam packagename contains underscore(s), that's not allowed in Yocto. We need a
# manual override here...
OPAM_PKGNAME_OVERRIDE="ppx_deriving_yojson"

#OPAM_VERBOSE = "1"
