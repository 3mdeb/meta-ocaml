DESCRIPTION = "Type-driven code generation for OCaml"
SECTION = "devel"
LICENSE = "MIT"

inherit opam_install

# opam packagename contains underscore(s), that's not allowed in Yocto.
# We need a manual override here...
OPAM_PKGNAME_OVERRIDE = "ppx_deriving"

# executables from this package run on the host system package, therefore we need
# to include ocaml-native instead of ocaml-cross
DEPENDS += " \
    ocaml-native \
    opam-cppo \
    opam-cppo-ocamlbuild \
    opam-ocamlbuild \
    opam-ppx-tools \
    opam-result \
    "
DEPENDS_remove = " \
    ocaml-cross-${TARGET_ARCH} \
    "

#OPAM_VERBOSE = "1"
