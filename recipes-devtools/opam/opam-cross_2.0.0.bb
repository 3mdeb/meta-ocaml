DESCRIPTION = "Package Manager for OCaml"
SECTION = "devel"
LICENSE = "GPLv2"

SRC_URI = " \
    https://github.com/ocaml/opam/archive/${PV}.tar.gz \
    file://0001-jbuilder-pathfix.patch \
    "

LIC_FILES_CHKSUM = "file://LICENSE;md5=d9e77cf0b09010013d038358f983b42a"

SRC_URI[md5sum] = "12b9835e43ff023a0b04a922f0ed9db5"
SRC_URI[sha256sum] = "1e9b47a66a952571b32217c58d0c58d2d66706083969dcfa1e42a60ecce30121"

# * opam has an autotools-style 'configure' file, but the makefile has been
#   created manually.
# * we inherit 'cross' because opam will run on the build system, but will
#   facilitate builds for the target system.
inherit autotools-brokensep cross

PROVIDES = "${TARGET_PREFIX}opam-cross"
PN = "opam-cross-${TARGET_ARCH}"

S = "${WORKDIR}/opam-${PV}"

DEPENDS += " \
    ocaml-native \
    bzip2-native \
    rsync-native \
    unzip-native \
    coreutils-native \
    "

# somehow this seems to be required...
PARALLEL_MAKE = "-j 1"

# create opam-root in work-shared directory
OPAM_ROOT = "${TMPDIR}/work-shared/ocaml/opam-root-${TARGET_SYS}"

# --prefix seems to be the only config option that's actually observed.
EXTRA_OECONF = "\
    --prefix=${prefix} \
    "

do_compile_prepend () {
    oe_runmake lib-ext
}

do_install () {
    oe_runmake install DESTDIR=${D}

    # initialize, create opam root in work-shared
    rm -rf ${OPAM_ROOT}
    ${D}${prefix}/bin/opam init \
        --disable-sandboxing \
        --root=${OPAM_ROOT} \
        --no-setup
}

sysroot_stage_all_append () {
    sysroot_stage_dir ${D}${prefix}/bin ${SYSROOT_DESTDIR}${prefix}/bin
}
